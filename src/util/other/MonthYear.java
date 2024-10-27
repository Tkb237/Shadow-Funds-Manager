package util.other;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

public class MonthYear
{
    private Year year;
    private Month month;
    private MonthYear(Month month, Year year)
    {
        this.month = month;
        this.year = year;
    }


    public Month getMonth() {
        return month;
    }

    public Year getYear() {
        return year;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public static MonthYear from(LocalDate date)
    {
        return new MonthYear(date.getMonth(), Year.of(date.getYear()));
    }

    public static MonthYear of(Month m, int year)
    {
        return new MonthYear(m, Year.of(year));
    }

    public static MonthYear now()
    {
        return new MonthYear(LocalDate.now().getMonth(), Year.now());
    }

    @Override
    public String toString()
    {
        return month.name() + " " + year;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return  true;
        if(obj instanceof MonthYear m)
        {
            return  m.getYear().equals(year) && m.getMonth().equals(month);
        }
        return false;
    }
}
