package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Sale;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("querySales")
@SessionScoped
public class QuerySalesBean implements Serializable{
    private static final long serialVersionUID = 1L;

	BLFacade facadeBL;

	private String searchText = "";
	private List<Sale> sales = new ArrayList<Sale>();
	private Sale selected;
	private boolean searched = false;

	private List<String> egoerak = new ArrayList<String>(Arrays.asList("Berria","Oso Ona","Egokia","Oso zaharra"));

	
	
	public QuerySalesBean() {
		facadeBL = FacadeBean.getBusinessLogic();
	}
	

	public void search() {
		Date today = UtilDate.trim(new Date());
		sales = facadeBL.getPublishedSales(searchText, today);
		searched = true;
	}

	public String getSearchText() { return searchText; }
	public void setSearchText(String searchText) { this.searchText = searchText; }

	public List<Sale> getSales() { return sales; }

	public boolean isSearched() { return searched; }
	
	public void setSelected(Sale sale) {
		this.selected = sale;
	}
	
	public Sale getSelected() {
		return this.selected;
	}
	
	public String getSelectedEgoera() {
	    if (selected == null) return "";
	    int status = selected.getStatus();
	    if (status < 0 || status >= egoerak.size()) return "";
	    return egoerak.get(status);
	}
	
	public String ikusi(Sale sale) {
		this.selected=sale;
		return "ShowSale";
	}
	
}

 