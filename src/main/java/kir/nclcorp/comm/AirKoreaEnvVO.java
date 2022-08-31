package kir.nclcorp.comm;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class AirKoreaEnvVO implements Serializable {

    private static final long serialVersionUID = 5313079143199778185L;
    private String dmX;
    private String dmY;
    private String mangName;
    private String addr;
    private String stationName;

}
