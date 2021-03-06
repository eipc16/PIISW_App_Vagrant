import { ObjectState } from '../infrastructure'

export interface MovieInterface {
    id: number;
    title: string;
    imdbId: string;
    objectState: ObjectState;
    releaseDate: Date;
    posterUrl: string;
}
