package beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import tp.ejb.BankDaoBean;
import tp.ejb.CityDaoBean;
import tp.ejb.CustomerDaoBean;
import tp.ejb.TransferDaoBean;
import domain.model.Bank;
import domain.model.Customer;
import domain.model.Transfer;

@ManagedBean(name = "transferWeb")
@SessionScoped
public class TransferWeb implements Serializable {

	private static final long serialVersionUID = 1654321856L;


	@EJB
	private TransferDaoBean transferDao;
	@EJB
	private BankDaoBean bankDao;
	@EJB
	private CustomerDaoBean customerDao;


	private List<Transfer> transfers;
	private Transfer currentTransfer;

	@EJB
	private CityDaoBean cityDao;


	public String modify() {
		transferDao.merge(currentTransfer);
		return "transfers";
	}

	public String next() {
		setCurrentTransfer(transferDao.next(getCurrentTransfer()));
		return "transfers";
	}

	public String prior() {
		setCurrentTransfer(transferDao.prior(getCurrentTransfer()));
		return "transfers";
	}

	public void customers(int bankId) {
		MBUtils.redirect("customers.xhtml?id=" + bankId);
	}

	
	public Transfer getCurrentTransfer() {
		if (currentTransfer == null)
			getAllBanks();
		return currentTransfer;
	}

	public void setCurrentTransfer(Transfer transfer) {
		this.currentTransfer = transferDao.find(transfer);
		//System.out.println("currentTransfer=" + this.currentTransfer.getName());
	}
	
	
	public boolean isCurrentTransfer(Bank bank) {
		return bank.equals(currentTransfer);
	}
	
	
	
	

	public List<Transfer> getAllBanks() {
		transfers = transferDao.getList();
		if (transfers.isEmpty()) {
			transferDao.populate();
			transfers = transferDao.getList();
		}
		if (!transfers.isEmpty() && currentTransfer == null)
			currentTransfer = transfers.get(0);
		return transfers;
	}
	
	
	public void transfer(Bank bank, Customer cus, double montant, String type){
		
		Transfer t = new Transfer();
		
		//SI l'utilisateur a demandé un retrait en liquide (choix 1)
		if(type=="1"){
		t.setCompte_banque(bank.getCompte_caisse());
		}
		//SI l'utilisateur a demandé un retrait en virement (choix 2)
		if(type=="2"){
		t.setCompte_banque(bank.getCompte_banque());
		}
		t.setCompte_client(cus.getAccounts().get(0));
		t.setMontant(montant);
		t.setDate(""+new Date());
		
		if(t.transfert()){
			transferDao.add(t);
		}
		
	}



	
	

	/**
	 * create the session before the Facelet (/JSP) page is printed-----
	 */

	@PostConstruct
	void initialiseSession() {
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}

}

/*
 * JSF 2 does enhance the h:commandButton by allowing the command to invoke an
 * associated action by passing a parameters for it. The main attribute of the
 * h:commandButton is an action attribute that accepts a method-binding
 * expression for a backing bean action (method) to be invoked when the user has
 * clicked on the button. The method-binding expression has the following roles
 * to be accepted as a proper action. The defined method (action) in the backing
 * bean should have a public access modifier. The defined method (action) in the
 * backing bean should have a String as returned type. (This string token will
 * be consumed by the JavaServer Faces Navigation Handler). Starting with JSF
 * 2.0, the defined method can accept a parameter values in the method
 * signature, this feature is useful for providing parameters to the actions of
 * buttons and links. When a method reference is evaluated, the parameters are
 * evaluated and passed to the method. The h:commandButton and h:commandLink are
 * the primary components for navigating within a JSF application, when a button
 * or link is clicked (activated), a POST request sends the form data back to
 * the server and the JSF framework lifecycle has started. The main difference
 * between h:commandButton and h:commandLink that the latter provides a
 * predefined code at the onclick JavaScript attribute
 */
