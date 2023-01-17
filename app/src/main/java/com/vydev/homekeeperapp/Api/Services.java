package com.vydev.homekeeperapp.Api;

import com.vydev.homekeeperapp.Api.Declaration.CalculatorService;
import com.vydev.homekeeperapp.Api.Declaration.LoginService;
import com.vydev.homekeeperapp.Api.Declaration.MonthDataService;
import com.vydev.homekeeperapp.Api.Declaration.UsersService;

public class Services {
    private static final LoginService loginService = Client.getInstance().create(LoginService.class);
    private static final UsersService usersService = Client.getInstance().create(UsersService.class);
    private static final MonthDataService monthDataService = Client.getInstance().create(MonthDataService.class);
    private static final CalculatorService calculatorService = Client.getInstance().create(CalculatorService.class);

    public static LoginService getLoginService() {
        return loginService;
    }

    public static UsersService getUsersService() {
        return usersService;
    }

    public static MonthDataService getMonthDataService() {
        return monthDataService;
    }

    public static CalculatorService getCalculatorService() {
        return calculatorService;
    }
}
