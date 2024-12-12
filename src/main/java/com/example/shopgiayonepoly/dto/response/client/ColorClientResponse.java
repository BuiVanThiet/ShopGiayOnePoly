package com.example.shopgiayonepoly.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColorClientResponse {
    private Integer idColor;
    private String nameColor;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorClientResponse that = (ColorClientResponse) o;
        return Objects.equals(idColor, that.idColor) && Objects.equals(nameColor, that.nameColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idColor, nameColor);
    }
}
