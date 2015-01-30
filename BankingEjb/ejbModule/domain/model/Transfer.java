package domain.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;


	@Entity
	@Table(name="Transfer")
	@NamedQueries( {
		@NamedQuery(name = "alltransfers", query = "select t FROM Transfer t")
	})

public class Transfer {
	
	private static final long serialVersionUID = -5494809610279467399L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private double montant;
	
	//Le compte de la banque à créditer
	@OneToOne
	private Account compte_banque;
	
	//Le compte du client à débiter
	@OneToOne
	private Account compte_client;
	//Date du Transfert
	private String date;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Account getCompte_banque() {
		return compte_banque;
	}


	public void setCompte_banque(Account compte_banque) {
		this.compte_banque = compte_banque;
	}


	public Account getCompte_client() {
		return compte_client;
	}


	public void setCompte_client(Account compte_client) {
		this.compte_client = compte_client;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Transfer() {
	
	}
	
	public Transfer(Account banque, Account client, double montant) {
		this.compte_banque=banque;
		this.compte_client=client;
		this.montant=montant;
		this.date= ""+new Date();
	}
	
	public Transfer(Account banque, Account client, double montant, String date) {
		this.compte_banque=banque;
		this.compte_client=client;
		this.montant=montant;
		this.date= date;
	}
	
	//On fait le choix ici que seul une methode existe, il suffit de rentrer le compte caisse ou le compte banque comme paramètre en fonction du type de retrait
	public boolean transfert(){
		boolean ok =false;
		
		if(montant <0 ){System.out.println("Le montant soit être strictement positif");}
		
		if(compte_banque!=null && compte_client!= null && montant>0){
			double old_banque = compte_banque.getBalance();
			double old_client = compte_client.getBalance();
			
			compte_banque.setBalance(compte_banque.getBalance()+montant);
			compte_client.setBalance(compte_client.getBalance()-montant);
			
			//On vérifie que le transfert a bien fonctionné
			
			if(compte_banque.getBalance()-old_banque== montant
					&& old_client-compte_client.getBalance()==montant
				){
				System.out.println("L'opération s'est déroulée avec succès");
				ok = true;
			}
			//Si ce n'est pas le cas, on rétablie les montant avant le transfert
			else{
				compte_banque.setBalance(old_banque);
				compte_banque.setBalance(old_client);
				System.out.println("Un problème est apparu lors de l'opération");
			}
		}
		return ok;
	}


	public double getMontant() {
		return montant;
	}


	public void setMontant(double montant) {
		this.montant = montant;
	}

}
