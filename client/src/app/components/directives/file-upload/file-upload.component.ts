import { Component, Input, ElementRef } from '@angular/core';
import { FileUploadService } from './../../../services/file-upload.service';

@Component({
    selector: 'file-upload',
    template: '<input type="file" [multiple]="multiple">'
})
export class FileUploadComponent {

    @Input() multiple:boolean = false;

    constructor(
		private fileUploadService:FileUploadService,
		private el:ElementRef
		) {}

    upload() {
        let inputEl:HTMLInputElement = this.el.nativeElement.querySelector('[type="file"]');
        let fileCount:number = inputEl.files.length;
        let formData = new FormData();
        if (fileCount > 0) {
            for (let i = 0; i < fileCount; i++) {
                formData.append('file[]', inputEl.files.item(i));
            }
			this.fileUploadService.testUpload((data) => {
				console.log(data);
			}, formData);
        }
    }

}