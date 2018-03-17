package goods_management;
import java.util.*;
public class GoodsProcess {
	
	List<InventoryType> inventory=new ArrayList<InventoryType>();
	List<ArrayList<String>> transactions=new ArrayList<ArrayList<String>>();
	List<ArrayList<String>> transactions_sorted;
	List<String> inv;
	List<String> shipping=new ArrayList<String>();
	List<String> errors=new ArrayList<String>();
	List<String> newInventory=new ArrayList<String>();
	
	class InventoryType{
		String itemNumber,supplier,description;
		int quantity;
	}
	
	class MyComparator implements Comparator<ArrayList<String>>{
		public int compare(ArrayList<String> a, ArrayList<String> b) {
			//System.out.println(c);
			
			if(a.get(0).equals("A")||b.get(0).equals("A")) {//When "==" is applied to 2 objects, it works to compare if there has the same address
				if((a.get(0).equals("A"))&&(!b.get(0).equals("A"))) {
					//System.out.println(-1);
					return -1;
					
				}
				else if((a.get(0).equals("A"))&&(b.get(0).equals("A"))){
					return 0;
				}
				else {
					return 1;
				}
			}
			else if(a.get(0).equals("R")||b.get(0).equals("R")) {
				if(a.get(0).equals("R")&&(!b.get(0).equals("R"))) {
					return -1;
				}
				else if((a.get(0).equals("R"))&&(b.get(0).equals("R"))){
					return 0;
				}
				else {
					return 1;
				}
			}
			else if(a.get(0).equals("O")||b.get(0).equals("O")) {
				if(a.get(0)=="O"&&(!b.get(0).equals("O"))) {
					return -1;
				}
				else if((a.get(0).equals("O"))&&(b.get(0).equals("O"))){
					return Integer.parseInt(a.get(2))-Integer.parseInt(b.get(2));
				}
				else {
					return 1;
				}
			}
			else {
				return 0;
			}
		}
	}
	
	
	
	
	GoodsProcess(ArrayList<String> inv,ArrayList<String> tra){
		for(int i=0;i<inv.size();i++) {
			String[] temp=inv.get(i).split("\\s+");
			InventoryType t=new InventoryType();
			inventory.add(t);
			inventory.get(i).itemNumber=temp[0];
			inventory.get(i).quantity= Integer.parseInt(temp[1]);
			inventory.get(i).supplier=temp[2];
			inventory.get(i).description=temp[3];
		}
		for(int i=0;i<tra.size();i++) {
			String[] temp=tra.get(i).split("\\s+");
			List<String> ls=Arrays.asList(temp); //forced conversion can cause a exception.
			ArrayList<String> t=new ArrayList<String>(ls);
			
			//t.addAll(ls);
			transactions.add(t);
		}

		transactions_sorted=new ArrayList<ArrayList<String>>(transactions);
		MyComparator mc=new MyComparator();
		transactions_sorted.sort(mc);
		//System.out.println(inventory);
		System.out.println(transactions);
		System.out.println(transactions_sorted);
		
		//newInventory=new ArrayList<String>(inv);
		
		this.inv=inv;
	}
	void process() {
		
		for(int i=0;i<transactions.size();i++) {
			switch(transactions.get(i).get(0)) {
				case "O":
					deliver(i);
					break;
				case "R":
					arrive(i);
					break;
				case "A":
					newType(i);
					break;
				case "D":
					delete(i);
					break;
			}
		}
		//System.out.println(inventory);
		genNewInv();
		System.out.println(newInventory);
		System.out.println(errors);
	}
	
	void newType(int index) {
		//String str=new String(transactions.get(index).get(1)+"\t0\t"+transactions.get(index).get(2)+"\t"+transactions.get(index).get(3));
		//newInventory.add(str);
		InventoryType temp=new InventoryType();
		temp.itemNumber=transactions.get(index).get(1);
		temp.supplier=transactions.get(index).get(2);
		temp.description=transactions.get(index).get(3);
		temp.quantity=0;
		inventory.add(temp);
	}
	
	void arrive(int index) {
		String item_number=transactions.get(index).get(1);
		int n_add=Integer.parseInt(transactions.get(index).get(2));
		for(int i=0;i<inventory.size();i++) {
			if(inventory.get(i).itemNumber.equals(item_number)) {
				
//				InventoryType entry=inventory.get(i);
//				String str=new String(entry.itemNumber+"\t"+String.valueOf(entry.quantity+n_add)+"\t"+entry.supplier+"\t"+entry.description);				
//				newInventory.set(i, str);
				inventory.get(i).quantity+=n_add;
				
			}
		}
	}
	
	void deliver(int index) {
		String item_number=transactions.get(index).get(1);
		int n_sub=Integer.parseInt(transactions.get(index).get(2));
		for(int i=0;i<inventory.size();i++) {
			if(inventory.get(i).itemNumber.equals(item_number)) {
//				InventoryType entry=inventory.get(i);
//				String str=new String(entry.itemNumber+"\t"+String.valueOf(entry.quantity-n_sub)+"\t"+entry.supplier+"\t"+entry.description);
//				newInventory.set(i, str);
				if(inventory.get(i).quantity-n_sub>=0) {
					inventory.get(i).quantity-=n_sub;
				}
				else {
					String str_err=new String(transactions.get(index).get(3)+"\t"+transactions.get(index).get(1)+"\t"+transactions.get(index).get(2));
					errors.add(str_err);
				}
			}
		}
	}
	
	void delete(int index) {
		String item_number=transactions.get(index).get(1);
		for(int i=0;i<inventory.size();i++) {
			if(inventory.get(i).itemNumber.equals(item_number)) {
//				InventoryType entry=inventory.get(i);
//				String str=new String(entry.itemNumber+"\t"+String.valueOf(entry.quantity-n_sub)+"\t"+entry.supplier+"\t"+entry.description);
//				newInventory.set(i, str);
				if(inventory.get(i).quantity==0) {
					inventory.remove(i);
				}
				else {
					String str_err=new String("0"+"\t"+transactions.get(index).get(1)+"\t"+inventory.get(i).quantity);
					errors.add(str_err);
				}
			}
		}
	}
	
	void genNewInv() {
		for(int i=0;i<inventory.size();i++) {
			String str=new String(inventory.get(i).itemNumber+"\t"+inventory.get(i).quantity+"\t"+inventory.get(i).supplier+"\t"+inventory.get(i).description);
			newInventory.add(str);
		}
	}
	
	
	
}
