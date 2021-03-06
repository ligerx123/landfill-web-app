import { AbstractEntity } from '../abstract-entity.class';
import { User } from '../user/user.class';

/**
 * This class was automatically generated from SurfaceEmissionExceedanceData.java using typescript-gen.
 * https://github.com/landfill-eforms/landfill-tools/tree/master/typescript-gen
 */
export abstract class SurfaceEmissionExceedanceData extends AbstractEntity {
	inspector:User;
	methaneLevel:number;
	dateTime:number;
	description:string;
}