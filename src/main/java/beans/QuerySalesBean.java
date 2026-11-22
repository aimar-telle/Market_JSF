package beans;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

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
	private String irudia;
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
	
	public String getIrudia() { return irudia; }
	
	public String getSelectedEgoera() {
	    if (selected == null) return "";
	    int status = selected.getStatus();
	    if (status < 0 || status >= egoerak.size()) return "";
	    return egoerak.get(status);
	}
	
	public String ikusi(Sale sale) {
		this.selected=sale;
		this.irudia=kargatuIrudia(sale);
		System.out.println(sale.getFile());
		return "ShowSale";
	}
	
	private String kargatuIrudia(Sale s) {
		try {
			String file = s.getFile();
	        System.out.println("kargatuIrudia: file = " + file);
			if (file == null || file.isEmpty()) return null;

			Image img = facadeBL.downloadImage(file);
	        System.out.println("kargatuIrudia: img = " + img);
			if (img == null) return null;

			BufferedImage bimg = (BufferedImage) img;
	        System.out.println("kargatuIrudia: " + bimg.getWidth() + "x" + bimg.getHeight()
            + " tipo=" + bimg.getType());
	        
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bimg, "png", baos);
	        boolean ok = ImageIO.write(bimg, "png", baos);
			System.out.println("kargatuIrudia: write=" + ok + " bytes=" + baos.size());

			return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}

 