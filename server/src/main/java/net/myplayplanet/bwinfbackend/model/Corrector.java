package net.myplayplanet.bwinfbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Corrector {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "shortName", unique = true)
    private String shortName;

    @Override
    public String toString() {
        return "Corrector{" +
                "id=" + id +
                ", shortName='" + shortName + '\'' +
                '}';
    }
}
