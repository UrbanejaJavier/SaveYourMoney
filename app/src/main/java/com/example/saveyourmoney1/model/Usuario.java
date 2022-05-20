package com.example.saveyourmoney1.model;

public class Usuario {

    private String uid;
    private String usuario;
    private String correo;
    private String contraseña;
    private String objetivo;
    private Double cantIngresos;
    private Double cantGastos;
    private Double cantAhorros;
    private Double gastoDeseo;

    public Usuario() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public Double getCantIngresos() {
        return cantIngresos;
    }

    public void setCantIngresos(Double cantIngresos) {
        this.cantIngresos = cantIngresos;
    }

    public Double getCantGastos() {
        return cantGastos;
    }

    public void setCantGastos(Double cantGastos) {
        this.cantGastos = cantGastos;
    }

    public Double getCantAhorros() {
        return cantAhorros;
    }

    public void setCantAhorros(Double cantAhorros) {
        this.cantAhorros = cantAhorros;
    }

    public Double getGastoDeseo() {
        return gastoDeseo;
    }

    public void setGastoDeseo(Double gastoDeseo) {
        this.gastoDeseo = gastoDeseo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "uid='" + uid + '\'' +
                ", usuario='" + usuario + '\'' +
                ", correo='" + correo + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", cantIngresos=" + cantIngresos +
                ", cantGastos=" + cantGastos +
                ", cantAhorros=" + cantAhorros +
                '}';
    }
}

