/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author robertthomure
 */
public class Outsourced extends Part {
    
    private String companyName;
    
    public Outsourced(int Id, String name, double price, int stock, 
            int min, int max, String companyName) {
        super(Id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}