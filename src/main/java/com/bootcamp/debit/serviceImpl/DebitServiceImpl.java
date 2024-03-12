package com.bootcamp.debit.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.debit.clients.AccountsRestClient;
import com.bootcamp.debit.clients.TransactionsRestClient;
import com.bootcamp.debit.dto.Account;
import com.bootcamp.debit.dto.DebitResponse;
import com.bootcamp.debit.dto.Transaction;
import com.bootcamp.debit.entity.Debit;
import com.bootcamp.debit.repository.DebitRepository;
import com.bootcamp.debit.service.DebitService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DebitServiceImpl implements DebitService{
	
    @Autowired
    private DebitRepository debitRepository;
    
    @Autowired
    AccountsRestClient accountsRestClient;
    
    @Autowired
    TransactionsRestClient transactionsRestClient;

    @Override
    public Flux<Debit> getAll() {
        return debitRepository.findAll();
    }

    @Override
    public Mono<Debit> geDebitById(String debitId) {
        return debitRepository.findById(debitId);
    }

    @Override
    public Mono<DebitResponse> saveDebit(Debit debit) {
    	DebitResponse response = new DebitResponse();
		response.setMessage("Error creating debit card");
		return accountsRestClient.getAccountById(debit.getAccountId().get(0)).flatMap(c -> {
			debit.setAmount(c.getAmount());
			debit.setCustomerId(c.getCustomerId());
			debit.setDebitDate(LocalDateTime.now());
			return debitRepository.save(debit).flatMap(d -> {
				response.setDebit(d);
				response.setMessage("Debit card created");
				return Mono.just(response);
			});
		}).defaultIfEmpty(new DebitResponse("account to associate does not exist",null));
    }

    @Override
    public Mono<Debit> updateDebit(Debit debit) {
        return debitRepository.findById(debit.getId())
                .flatMap(newDebit -> {
                    newDebit.setId(debit.getId());
                    newDebit.setCustomerId(debit.getCustomerId());
                    newDebit.setAmount(debit.getAmount());
                    newDebit.setCardNumber(debit.getCardNumber());
                    newDebit.setDebitDate(debit.getDebitDate());
                    newDebit.setAccountId(debit.getAccountId());
                    return debitRepository.save(newDebit);
        });
    }

    @Override
    public Mono<Debit> deleteDebit(String debitId) {
        return debitRepository.findById(debitId)
                .flatMap(debit -> debitRepository.delete(debit)
                        .then(Mono.just(debit)));
    }
    
    @Override
    public Mono<Debit> getDebitByIdCustomer(String customerId){
        return debitRepository.findAll()
        		.filter(debit -> debit.getCustomerId().equals(customerId)).next();
    }
    
    @Override
    public Flux<Debit> getAllDebitByIdConsumer(String customerId){
        return debitRepository.findAll()
        		.filter(debit -> debit.getCustomerId().equals(customerId));
    }

	@Override
	public Mono<DebitResponse> updateDebitDeposit(Debit debit) {
		DebitResponse response = new DebitResponse();
		return debitRepository.findById(debit.getId()).flatMap(deb -> {
			Double mount=deb.getAmount() + debit.getAmount();
			deb.setAmount(mount);
			return debitRepository.save(deb).flatMap(upd -> {
				return accountsRestClient.getAccountById(upd.getAccountId().get(0)).flatMap(account -> {
					account.setAmount(account.getAmount() + debit.getAmount());
					return accountsRestClient.updateAccount(account).flatMap(x -> {
						response.setMessage("successful Deposit");
						response.setDebit(upd);
						return registerTransaction(upd, debit.getAmount(), "DEPOSITO", account);
					});
				});
			});
		}).defaultIfEmpty(new DebitResponse("Debit Card no exist", null));
	}
	
	@Override
	public Mono<DebitResponse> updateDebitPay(Debit debit) {
		DebitResponse response = new DebitResponse();
		return debitRepository.findById(debit.getId()).flatMap(deb -> {
			String idAcc = deb.getAccountId().get(0);
			response.setMessage("You don't have enough balance");
			response.setDebit(deb);
			return getAccountMount(deb,debit).flatMap(b -> {
				Double newMountDebit = deb.getAmount() - debit.getAmount();
				Double newMount = b.getAmount()-debit.getAmount();
				b.setAmount(newMount);
				return accountsRestClient.updateAccount(b).flatMap(x -> {
					response.setMessage("successful Pay");
					response.setDebit(deb);
					if(idAcc.equals(b.getId())) {
						deb.setAmount(newMountDebit);
						return debitRepository.save(deb).flatMap(upd -> {
							return registerTransaction(upd, debit.getAmount(), "RETIRO", b);
						});
					}
					return registerTransaction(deb, debit.getAmount(), "RETIRO", b);
				});
			}).defaultIfEmpty(response);
		});
	}
	
	private Mono<DebitResponse> registerTransaction(Debit debit, Double amount, String typeTransaction, Account accountt){
		Transaction transaction = new Transaction();
		transaction.setCustomerId(debit.getCustomerId());
		transaction.setProductId(debit.getId());
		transaction.setProductType(debit.getProductType());
		transaction.setTransactionType(typeTransaction);
		transaction.setAmount(amount);
		transaction.setCustomerType(debit.getCustomerType());
		transaction.setBalance(debit.getAmount());
		transaction.setTransactionDate(LocalDateTime.now());
		return transactionsRestClient.createTransaction(transaction).flatMap(t -> {
			transaction.setProductId(accountt.getId());
			transaction.setProductType(accountt.getDescripTypeAccount());
			return transactionsRestClient.createTransaction(transaction).flatMap(d -> {
				return Mono.just(new DebitResponse("Successful transaction", debit));
			});
        });
	}
	
	private Mono<Account> getAccountMount(Debit debit,Debit debitNew) {
        return accountsRestClient.getAllAccountXCustomerId(debit.getCustomerId())
                .filter(d -> debit.getAccountId().contains(d.getId()))
                .filter(d -> d.getAmount() >= debitNew.getAmount())
                .sort((p1, p2) -> Integer.compare(debit.getAccountId().indexOf(p1.getId()), debit.getAccountId().indexOf(p2.getId())))
                .next();
    }
    
}
