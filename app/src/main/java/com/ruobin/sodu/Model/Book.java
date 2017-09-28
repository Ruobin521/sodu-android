package com.ruobin.sodu.Model;

import java.util.List;

/**
 * Created by ruobin on 2017/9/26.
 */
public class Book {

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


}
