package com.piisw.cinema_tickets_app.api;

import com.piisw.cinema_tickets_app.domain.reservation.entity.ReservationState;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SeatDTO {

    @ApiModelProperty(readOnly = true)
    private Long id;

    @NotNull
    private Long seatNumber;

    @NotNull
    private Long rowNumber;

    @NotNull
    private ReservationState reservationState;

}
