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
public class SizeClientResponse {
    private Integer idSize;
    private String nameSize;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SizeClientResponse that = (SizeClientResponse) o;
        return Objects.equals(idSize, that.idSize) && Objects.equals(nameSize, that.nameSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSize, nameSize);
    }
}
