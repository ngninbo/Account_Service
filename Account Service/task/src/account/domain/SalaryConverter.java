package account.domain;

public class SalaryConverter {

    public static String convert(Long salary) {
        Salary sal = Salary.fromLong(salary);
        return String.format("%s dollar(s) %s cent(s)", sal.getDollar(), sal.getCent());
    }
}
