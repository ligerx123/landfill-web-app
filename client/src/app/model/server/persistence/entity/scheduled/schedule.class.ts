import { AbstractEntity } from '../abstract-entity.class';
import { SchedulePeriodBoundary } from '../../enums/scheduled/schedule-period-boundary.enum';
import { ScheduleRecurrence } from '../../enums/scheduled/schedule-recurrence.enum';

/**
 * This class was automatically generated from Schedule.java using typescript-gen.
 * https://github.com/landfill-eforms/landfill-tools/tree/master/typescript-gen
 */
export class Schedule extends AbstractEntity {
	recurrence:ScheduleRecurrence;
	offset:number;
	periodBoundary:SchedulePeriodBoundary;
	frequency:number;
	active:boolean;
	lastOccurrence:number;
}