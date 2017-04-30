import { ImeNumber } from './../../../model/server/persistence/entity/surfaceemission/instantaneous/ime-number.class';
import { MdSnackBar } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { ImeNumberService } from './../../../services/instantaneous/ime-number.service';
import { Component, OnInit } from '@angular/core';


@Component({
	selector: 'app-ime-number',
	templateUrl: './ime-number.component.html',
	styleUrls: ['./ime-number.component.scss']

})
export class ImeNumberComponent implements OnInit {

	isDataLoaded:boolean;
	imeNumber:string;
	imeNumberData:ImeNumber = new ImeNumber();

	constructor(
		private imeNumberService:ImeNumberService,
		private activatedRoute:ActivatedRoute,
		private snackBar:MdSnackBar,
	) {}

	ngOnInit() {
		// TODO Display error message if IME number in the URL is invalid.
		this.imeNumber = this.activatedRoute.params['_value']['imeNumber'];
		console.log(this.imeNumber);
		this.imeNumberService.getByImeNumber(this.imeNumber,
			(data) => {
				console.log(data);
				this.imeNumberData = data;
				this.isDataLoaded = true;
			}
		);
	}

	save() {
		this.imeNumberService.update(this.imeNumberData, 
			(data) => {
				console.log(data);
				this.snackBar.open("IME entries have been updated.", "OK", {duration: 2000});
			}
		);
	}

}