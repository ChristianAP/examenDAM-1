package com.example.examendam.Modelo;

public class Mascota {
    private String idmascota;
    private String mascota;
    private String raza;
    private String dueño;
    private String telefono;

    @Override
    public String toString() {
        return mascota;
    }

    public String getIdmascota() {
        return idmascota;
    }

    public void setIdmascota(String idmascota) {
        this.idmascota = idmascota;
    }

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getDueño() {
        return dueño;
    }

    public void setDueño(String dueño) {
        this.dueño = dueño;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
