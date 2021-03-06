import {handleResponse} from './response-handler'
import {appConfig} from '../config'
import {Seat} from "../models/screening-rooms";
import {AuthenticationService, AuthenticationServiceImpl} from "./auth-service";
import {Resource} from "../models/infrastructure";
import {Reservation} from "../models/reservation";

export interface SeatService {
    fetchSeats(screeningId: number): Promise<Seat[]>;
    reserveSeats(reservation: Reservation): Promise<Resource>;
}

export class SeatServiceImpl implements SeatService {
    static seatService: SeatService;
    private authService: AuthenticationService;

    private constructor (authenticationService?: AuthenticationService) {
        this.authService = authenticationService || AuthenticationServiceImpl.createInstance();
    }

    static createInstance(authenticationService?: AuthenticationService): SeatService {
        if (!SeatServiceImpl.seatService) {
            SeatServiceImpl.seatService = new SeatServiceImpl(authenticationService)
        }
        return SeatServiceImpl.seatService
    }

    fetchSeats(screeningId: number): Promise<Seat[]> {
        const requestOptions = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', 'Authorization': this.authService.getCurrentTokenAsString() }
        };
        return fetch(`${appConfig.apiUrl}/seats/screening/${screeningId}`, requestOptions)
            .then(handleResponse)
            .then((seats: Seat[]) => seats)
    }

    reserveSeats(reservation: Reservation): Promise<Resource> {
        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'Authorization': this.authService.getCurrentTokenAsString()},
            body: JSON.stringify(reservation)
        };
        return fetch(`${appConfig.apiUrl}/reservation`, requestOptions)
            .then(handleResponse)
            .then((resource: Resource) => resource)
    }
}
