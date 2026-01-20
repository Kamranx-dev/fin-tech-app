package com.safarov.tech_app.dto.response.mbdto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@XmlRootElement(name = "Valute")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValuteResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "Code")
    String code;
    @XmlElement(name = "Nominal")
    BigDecimal nominal;
    @XmlElement(name = "Name")
    String name;
    @XmlElement(name = "Value")
    BigDecimal value;

}
