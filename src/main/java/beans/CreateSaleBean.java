package beans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.primefaces.model.file.UploadedFile;

import businessLogic.BLFacade;
import exceptions.MustBeLaterThanTodayException;
import exceptions.ParamNullException;
import exceptions.SaleAlreadyExistException;
import gui.Utils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("createSale")
@SessionScoped
public class CreateSaleBean implements Serializable {
    private static final long serialVersionUID = 1L;

    BLFacade facadeBL;

    private String izena;
    private String deskribapena;
    private float prezioa;
    private Date data;
    private String egoera;
    private UploadedFile argazkia;

    private List<String> egoerak = new ArrayList<String>(Arrays.asList("Berria","Oso Ona","Egokia","Oso zaharra"));

    private String sellerMail = "seller1@gmail.com";

    public CreateSaleBean() {
        facadeBL = FacadeBean.getBusinessLogic();
        data = new Date();
        System.out.println(egoerak);
    }

    public String sortuEskaintza() {
        try {
            File file = null;

            if (argazkia != null && argazkia.getSize() > 0) {
                file = new File(argazkia.getFileName());

                try (InputStream input = argazkia.getInputStream()) {
                    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            int numEgoera = egoerak.indexOf(egoera);

            facadeBL.createSale(
                izena,
                deskribapena,
                numEgoera,
                prezioa,
                data,
                sellerMail,
                file
            );

            return "Hasiera";

        } catch (ParamNullException | MustBeLaterThanTodayException |
                 SaleAlreadyExistException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getIzenburua() { return izena; }
    public void setIzenburua(String izena) { this.izena = izena; }

    public String getDeskribapena() { return deskribapena; }
    public void setDeskribapena(String deskribapena) { this.deskribapena = deskribapena; }

    public float getPrezioa() { return prezioa; }
    public void setPrezioa(float prezioa) { this.prezioa = prezioa; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public String getEgoera() { return egoera; }
    public void setEgoera(String egoera) { this.egoera = egoera; }

    public UploadedFile getArgazkia() { return argazkia; }
    public void setArgazkia(UploadedFile argazkia) { this.argazkia = argazkia; }

    public List<String> getEgoerak() {
        return egoerak;
    }
    public String getSellerMail() { return sellerMail; }
    public void setSellerMail(String sellerMail) { this.sellerMail = sellerMail; }

}