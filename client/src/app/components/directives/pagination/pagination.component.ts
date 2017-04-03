import { Component, Input, OnChanges } from '@angular/core';

@Component({
    selector: 'app-pagination',
    templateUrl: './pagination.component.html',
    styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements OnChanges {

    @Input() pagination:Pagination = new Pagination();
    @Input() totalRows:number = 0;
    @Input() extraPadding:number = 0;
    @Input() showPageSelector:boolean = false;
    @Input() showFirstLastPage:boolean = false;

    readonly displayedRange:{min:number, max:number} = {
        min: 1,
        max: 1
    };

    readonly displayedRowsSelection:number[] = [10, 25, 50, 100];

    availablePagesSelection:number[] = [1];

	ngOnChanges() {
        console.log("Pagination change detected.");
        this.validate();
        this.updateRange();
	}

    validate() {
        if (this.pagination.currentPage < 1) {
            this.pagination.currentPage = 1;
        }
        let pageCount = ~~(this.totalRows / this.pagination.displayedRows) + 1;
        if (this.pagination.currentPage > pageCount) {
            this.pagination.currentPage = pageCount;
        }
    }

    updateRange() {
        let pageMax = this.pagination.currentPage * this.pagination.displayedRows;
        this.displayedRange.max = pageMax > this.totalRows ? this.totalRows : pageMax;
        this.displayedRange.min = (this.pagination.currentPage - 1) * this.pagination.displayedRows + 1;
        this.availablePagesSelection = this.generateAvaliablePagesArray();
    }

    generateAvaliablePagesArray():number[] {
        let pageCount = ~~(this.totalRows / this.pagination.displayedRows) + 1;
        return Array(pageCount).fill(0).map((x,i) => i + 1);
    }

    changeDisplayedRows() {
        this.validate();
        this.updateRange();
    }

    changeCurrentPage() {
        this.validate();
    }

    firstPage() {
        this.pagination.currentPage = 1;
        this.updateRange();
    }

    prevPage() {
        if (this.pagination.currentPage > 1) {
            this.pagination.currentPage--;
            this.updateRange();
        }
    }

    nextPage() {
        let pageCount = ~~(this.totalRows / this.pagination.displayedRows) + 1;
        if (this.pagination.currentPage < pageCount) {
            this.pagination.currentPage++;
            this.updateRange();
        }
    }

    lastPage() {
        this.pagination.currentPage = ~~(this.totalRows / this.pagination.displayedRows) + 1;
        this.updateRange();
    }

}

export class Pagination {
    displayedRows:number = 10;
    currentPage:number = 1;
}