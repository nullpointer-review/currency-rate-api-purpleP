package com.example.test;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by mihaildoronin on 14.10.15.
 */
@RestController
public class CurrencyController {

    @RequestMapping(value="/rate/{code}/{date}", method= RequestMethod.GET)
    public CurrencyResponse getCurrencyRage(@PathVariable("code") String code,
                                            @PathVariable("date")
                                            @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) Date date) {
        return new CurrencyResponse(code, date, 60);
    }
}
