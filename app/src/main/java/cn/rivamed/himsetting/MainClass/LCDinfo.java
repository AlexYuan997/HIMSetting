package cn.rivamed.himsetting.MainClass;

import org.litepal.crud.DataSupport;

public class LCDinfo extends DataSupport{
    private int number;
    private String serialnumber;
    private  String fristtxt;
    private  String secondtxt;


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getFristtxt() {
        return fristtxt;
    }

    public void setFristtxt(String fristtxt) {
        this.fristtxt = fristtxt;
    }

    public String getSecondtxt() {
        return secondtxt;
    }

    public void setSecondtxt(String secondtxt) {
        this.secondtxt = secondtxt;
    }

    @Override
    public String toString() {
        return "LCDinfo{" +
                "number=" + number +
                ", serialnumber='" + serialnumber + '\'' +
                ", fristtxt='" + fristtxt + '\'' +
                ", secondtxt='" + secondtxt + '\'' +
                '}';
    }
}
