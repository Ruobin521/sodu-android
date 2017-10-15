package com.ruobin.sodu.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ruobin on 2017/9/26.
 */
public class Book implements Serializable  {

    public int Id;

    public String BookName ;
    public String BookId ;
    public boolean IsNew;


    public String NewestCatalogName;
    public String NewestCatalogUrl ;
    public String LastReadCatalogName;
    public String LastReadCatalogUrl;
    public String UpdateTime;

    public String RankValue ;

    public String Author ;
    public String Cover;
    public String Description;
    public String LyWeb ;

    public String SoDuUpdateCatalogUrl;

    private boolean IsLocal;

    private boolean IsHistory;

    private boolean IsOnline;

    private boolean IsTxt;

    public List<BookCatalog> CatalogList ;


    public Book clone(){
       Book temp = new Book();
        temp.BookId = this.BookId;
        temp.BookName = this.BookName;
        temp.NewestCatalogName = this.NewestCatalogName;
        temp.NewestCatalogUrl = this.NewestCatalogUrl;
        temp.LastReadCatalogName = this.LastReadCatalogName;
        temp.LastReadCatalogUrl = this.LastReadCatalogUrl;
        temp.UpdateTime = this.UpdateTime;
        temp.Author = this.Author;
        temp.Cover = this.Cover;
        temp.Description = this.Description;
        temp.LyWeb = this.LyWeb;
        temp.SoDuUpdateCatalogUrl = this.SoDuUpdateCatalogUrl;
        return  temp;
    }
}
