package com.api.alquilaya.models;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name="mantenimiento")
public class MantenimientoModel {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long idMantenimiento;
 @ManyToOne(fetch=FetchType.EAGER,optional=false) @JoinColumn(name="id_vehiculo",nullable=false) private VehiculoModel vehiculo;
 @Column(nullable=false) private LocalDate fechaMantenimiento;
 @Column(nullable=false,length=500) private String descripcion;
 @Column(nullable=false,precision=10,scale=2) private BigDecimal costo;
 private Integer kilometraje;
 public Long getIdMantenimiento(){return idMantenimiento;}
 public void setIdMantenimiento(Long v){idMantenimiento=v;}
 public VehiculoModel getVehiculo(){return vehiculo;}
 public void setVehiculo(VehiculoModel v){vehiculo=v;}
 public LocalDate getFechaMantenimiento(){return fechaMantenimiento;}
 public void setFechaMantenimiento(LocalDate v){fechaMantenimiento=v;}
 public String getDescripcion(){return descripcion;}
 public void setDescripcion(String v){descripcion=v;}
 public BigDecimal getCosto(){return costo;}
 public void setCosto(BigDecimal v){costo=v;}
 public Integer getKilometraje(){return kilometraje;}
 public void setKilometraje(Integer v){kilometraje=v;}
}
