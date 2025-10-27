package com.example.fantasticosback.dto.response;
public class SubjectDTO  {
    private String name;
    private int credits;
    private int semester;
    private String code;



    // Constructor para crear (sin grupos)
    public SubjectDTO(String code, String name, int credits, int semester) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.semester = semester;
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
