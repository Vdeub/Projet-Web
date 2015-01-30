package tp.ejb;

import java.util.List;

import javax.ejb.Remote;

import domain.model.Transfer;


@Remote
public interface TransferDao {
	public List<Transfer> getList();
	public Transfer add(Transfer t);
	public Transfer find(int id);
	public Transfer delete(Transfer t);
	public Transfer first();
	public Transfer last();
	public Transfer prior(Transfer t);
	public Transfer next(Transfer t);
	public Transfer clone(Transfer t);
	public Transfer create();
	public Transfer merge(Transfer t);
	public Transfer foobar(Transfer t);
	public Transfer createRandom();
	public Boolean isSame(Transfer a, Transfer t);
	public List<Transfer> populate();
}
