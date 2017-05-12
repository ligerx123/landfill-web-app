import { AbstractEntity } from '../abstract-entity.class';
import { ExceedanceType } from '../../enums/exceedance/exceedance-type.enum';
import { Site } from '../../enums/location/site.enum';
import { ReportType } from '../../enums/report/report-type.enum';

/**
 * This class was automatically generated from ReportQuery.java using typescript-gen.
 * https://github.com/landfill-eforms/landfill-tools/tree/master/typescript-gen
 */
export abstract class ReportQuery extends AbstractEntity {
	reportType:ReportType;
	site:Site;
	exceedanceTypes:ExceedanceType[];
	startDate:number;
	endDate:number;
	dateCreated:number;
}