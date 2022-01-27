package KitapListe;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	public int SayfaSayisi=0;
	public ArrayList<kitap> kitapAra(int sayfaSayi,String AranacakKelime) {
		String s="https://www.kitapyurdu.com/index.php?route=product/search&filter_name="+AranacakKelime;
		s+="&page="+sayfaSayi;

		ArrayList<kitap> kitapList=new ArrayList<kitap>();
		 try {
			Document document = Jsoup.connect(s).get();
			Elements kitaplar=document.select("#product-table").select(".product-cr");
			SayfaSayisi=Integer.parseInt(document.select(".results").text().split(" ")[7].replace("(", ""));
			for(Element e:kitaplar) {
				kitap ktp=new kitap();
				ktp.kitapAdi=e.getElementsByClass("name").select("span").text();
				ktp.isbnNo="NULL";
				ktp.sayfaSayisi="NULL";
				for (Element w:e.getElementsByClass("author").select(".alt").select("span")) {
					if (ktp.yazarAdi== null) {
						ktp.yazarAdi=w.text();
					}
					else {
						ktp.yazarAdi+=" - "+w.text();
					}
					
				}
				if (ktp.yazarAdi==null) {
					ktp.yazarAdi="NULL";
				}
				ktp.yayineviAdi=e.getElementsByClass("publisher").select(".alt").select("span").text();
				String[] product=e.getElementsByClass("product-info").text().split("\\| ");
				
				int isbnNo=0;
				int sayfa=2;
				if(product.length==6) {
					isbnNo++;
					sayfa++;
				}				
				if(product.length>4)
				ktp.isbnNo=product[isbnNo];
				if(product.length>3)
					ktp.sayfaSayisi=product[sayfa];
				ktp.resimLink=e.getElementsByTag("img").attr("src");
				ktp.sayfaSayisi=ktp.sayfaSayisi.replace(" ", "");
				ktp.isbnNo=ktp.isbnNo.replace(" ", "");
				for (int i = 0; i < ktp.isbnNo.length(); i++) {
					 if (!Character.isDigit(ktp.isbnNo.charAt(i)))
                     {
						 ktp.isbnNo="NULL";
                             break;
                     }
				}
				for (int i = 0; i < ktp.sayfaSayisi.length(); i++) {
					 if (!Character.isDigit(ktp.sayfaSayisi.charAt(i)))
                    {
						ktp.sayfaSayisi="NULL";
                            break;
                    }
				}
				kitapList.add(ktp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		 return kitapList;
	}
	public int sayfaSayiBul() {
		return SayfaSayisi;
	}
	
}
