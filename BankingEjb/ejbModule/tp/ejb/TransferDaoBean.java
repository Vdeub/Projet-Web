package tp.ejb;

import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import domain.model.Account;
import domain.model.Bank;
import domain.model.Customer;
import domain.model.Transfer;
import tp.ejb.utils.Paging;
import tp.ejb.utils.RandomString;

@Stateless
public  class TransferDaoBean implements TransferDao {

	private static final long serialVersionUID = -5443611226894567253L;

	@PersistenceContext(unitName = "TransferingPU")
	private EntityManager em;
	
	
	@EJB
	CustomerDao  customerDao;
	
	@EJB
	AccountDao  accountDao;
	
	
	@EJB
	CityDao  cityDao;
	
	@EJB
	BankDao  bankDao;
	
	public Transfer find(Transfer t) {
		return em.find(Transfer.class, t.getId());
	}

	public TransferDaoBean() {
		super();
		System.out.println("creating TransferDaoBean");
	}


	public Transfer add(Transfer t) {
		if (find(t) == null) {
			em.persist(t);
			return t;
		} else
			return null;
	}

	public Transfer clone(Transfer t) {
		Transfer newb = new Transfer(t.getCompte_banque(), t.getCompte_client(), t.getMontant(), t.getDate());
		em.persist(newb);
		return newb;
	}

	public Transfer create() {
		Transfer newb = new Transfer();
		em.persist(newb);
		return newb;
	}

	
	public Transfer save(Transfer t) {
		em.merge(t);
		return t;
	}
	
	
	private Random rand = new Random();

	public Transfer createRandom() {
		Transfer t = new Transfer();
		Account banque = new Account();
		Account client = new Account();
		banque.setBalance(Math.random()*100);
		banque.setBalance(Math.random()*100);
		double montant = Math.random()*10;
		
		t.setCompte_banque(banque);
		t.setCompte_client(client);
		t.setMontant(montant);
		em.persist(t);
		return t;
	}

	public Transfer delete(Transfer t) {
		t = find(t); //pour avoir un bean rattach�
		Transfer pr = prior(t);
		if (pr == null)
			pr = next(t);
		em.remove(t);
		return pr;
	}

	public Transfer find(int id) {
		return em.find(Transfer.class, id);
	}

	public Transfer first() {
		List<Transfer> bks = getList();
		return bks.get(0);
	}

	public Transfer next(Transfer t) {
		return Paging.next(getList(),t);
	}

	public Transfer prior(Transfer t) {
		return Paging.prior(getList(),t);
	}

	@SuppressWarnings("unchecked")
	public List<Transfer> getList() {
		return em.createNamedQuery("allbanks").getResultList();
	}

	public Transfer last() {
		List<Transfer> bks = getList();
		return bks.get(bks.size() - 1);
	}



	public Transfer foobar(Transfer t) {
		// TODO Auto-generated method stub
		return t;
	}

	public Transfer merge(Transfer t) {
		if (find(t) != null) {
			em.merge(t);
			return t;
		} else
			return null;
	}

	public List<Transfer> populate() {
		
		cityDao.populate();
		bankDao.populate();
		
		
		List<Bank> banques = bankDao.getList();
		
		customerDao.populate(banques.get(0), 0);
		
		List<Customer> clients = customerDao.getList(banques.get(0));
		List<Account> comptes = accountDao.populate(clients.get(0));
		
		Transfer t = create();
		banques.get(0).getCompte_caisse().setBalance(Math.random()*100);
		comptes.get(0).setBalance(Math.random()*100);
		double montant = Math.random()*10;
		
		//Ici on imagine un retrait d'argent en liqueide, c'est donc le compte "caisse" qui est crédité
		t.setCompte_banque(banques.get(0).getCompte_caisse());
		t.setCompte_client(comptes.get(0));
		t.setMontant(montant);


		for (int i = 0; i < 10; i++) {
			t = createRandom();
			em.merge(t);
		}
		return getList();
	}

	public Boolean isSame(Transfer a, Transfer t) {
		return a!=null && t!=null && a.getId() == t.getId();
	}





}
