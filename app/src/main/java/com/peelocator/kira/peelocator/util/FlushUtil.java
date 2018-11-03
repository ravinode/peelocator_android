package com.peelocator.kira.peelocator.util;

import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;


public class FlushUtil {

    public static String getID()
    {
        UUID id = UUID.randomUUID();
        String date = new SimpleDateFormat("yyMMddHHmmssZ").format(new Date());
        return id+"-"+date;
    }
}
