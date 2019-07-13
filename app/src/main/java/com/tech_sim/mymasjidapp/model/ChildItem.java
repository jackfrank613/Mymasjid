
package com.tech_sim.mymasjidapp.model;
public class ChildItem {
    String FatherName;
    String StudentName;
    String StudentAge;
    String FatherContectNO;
    String ClassDays;
    String ChildMasjid;
    String ChildId;
    String Date;

    String pin;



    private boolean check=true;
    public ChildItem(String studentName,String fatherName,  String studentAge, String fatherContectNO,
                     String classDays, String childMasjid, String childId,String date,String pin) {
        FatherName = fatherName;
        StudentName = studentName;
        StudentAge = studentAge;
        FatherContectNO = fatherContectNO;
        ClassDays = classDays;
        ChildMasjid = childMasjid;
        ChildId = childId;
        Date=date;
        this.pin=pin;
    }

    public String getFatherName() {
        return FatherName;
    }

    public String getStudentName() {
        return StudentName;
    }

    public String getStudentAge() {
        return StudentAge;
    }

    public String getFatherContectNO() {
        return FatherContectNO;
    }

    public String getClassDays() {
        return ClassDays;
    }

    public String getChildMasjid() {
        return ChildMasjid;
    }

    public String getDate() {
        return Date;
    }

    public String getChildId() {
        return ChildId;
    }
    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}




