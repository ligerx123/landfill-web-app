import { EditUnverifiedProbeDialogComponent } from './components/unverified-data/dialog/edit-unverified-probe-dialog/edit-unverified-probe-dialog.component';
import { ScheduledReportDialogComponent } from './components/report/dialog/scheduled-report-dialog/scheduled-report-dialog.component';
import { ScheduledEmailService } from './services/scheduled/scheduled-email.service';
import { ApplicationSettingsCanDeactivate } from './components/settings/application-settings/application-settings.candeactivate';
import { ImeRecheckDialogComponent } from './components/exceedance/dialog/ime-recheck-dialog/ime-recheck-dialog.component';
import { ImeRepairDialogComponent } from './components/exceedance/dialog/ime-repair-dialog/ime-repair-dialog.component';
import { IseRecheckDialogComponent } from './components/exceedance/dialog/ise-recheck-dialog/ise-recheck-dialog.component';
import { IseRepairDialogComponent } from './components/exceedance/dialog/ise-repair-dialog/ise-repair-dialog.component';
import { SuperAdminPasswordDialogComponent } from './components/settings/dialog/super-admin-password-dialog/super-admin-password-dialog.component';
import { SettingsModule } from './components/settings/settings.module';
import { ApplicationSettingsComponent } from './components/settings/application-settings/application-settings.component';
import { ApplicationSettingsService } from './services/app/application-settings.service';
import { FlexLayoutCustomBreakpointsModule } from './components/flex-layout/flex-layout-custom-breakpoints.module';
import { EditUnverifiedIntegratedDialogComponent } from './components/unverified-data/dialog/edit-unverified-integrated-dialog/edit-unverified-integrated-dialog.component';
import { EditUnverifiedWarmspotDialogComponent } from './components/unverified-data/dialog/edit-unverified-warmspot-dialog/edit-unverified-warmspot-dialog.component';
import { EditUnverifiedInstantaneousDialogComponent } from './components/unverified-data/dialog/edit-unverified-instantaneous-dialog/edit-unverified-instantaneous-dialog.component';
import { UserStatusDialogComponent } from './components/user/dialog/user-status-dialog/user-status-dialog.component';
import { UserPasswordDialogComponent } from './components/user/dialog/user-password-dialog/user-password-dialog.component';
import { UserActivityService } from './services/user/user-activity.service';
import { UserGroupDialogComponent } from './components/user/dialog/user-group-dialog/user-group-dialog.component';
import { UserDialogComponent } from './components/user/dialog/user-dialog/user-dialog.component';
import { InstrumentTypeDialogComponent } from './components/instrument/dialog/instrument-type-dialog/instrument-type-dialog.component';
import { InstrumentDialogComponent } from './components/instrument/dialog/instrument-dialog/instrument-dialog.component';
import { DatePickerComponent } from './components/directives/pickers/date-picker/date-picker.component';
import { FileDownloadService } from './services/file/file-download.service';
import { OkDialogComponent } from './components/directives/dialogs/ok-dialog/ok-dialog.component';
import { YesNoDialogComponent } from './components/directives/dialogs/yes-no-dialog/yes-no-dialog.component';
import { PublicModule } from './components/public/public.module'
import { IseNumberService } from './services/integrated/ise-number.service';
import { IntegratedDataService } from './services/integrated/integrated-data.service';
import { WarmspotDataService } from './services/instantaneous/warmspot-data.service';
import { NavigationService } from './services/app/navigation.service';
import { TitleService } from './services/app/title.service';
import { PaginationComponent } from './components/directives/pagination/pagination.component';
import { MobileModule } from './components/mobile/mobile.module';
import { InstrumentTypeService } from './services/instrument/instrument-type.service';
import { InstrumentModule } from './components/instrument/instrument.module';
import { UnverifiedDataService } from './services/unverified/unverified-data-set.service';
import { UserService } from './services/user/user.service';
import { ReportService } from './services/report/report.service';
import { UserGroupService } from './services/user/user-group.service';
import { InstrumentService } from './services/instrument/instrument.service';
import { InstantaneousDataService } from './services/instantaneous/instantaneous-data.service';
import { ImeNumberService } from './services/instantaneous/ime-number.service';
import { CommonModule } from './components/common/common.module';
import { UnverifiedDataModule } from './components/unverified-data/unverified-data.module';
import { ReportModule } from './components/report/report.module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '@angular/material'
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { AppRoutes, Routing, AppRouterProviders } from './app.routing';
import { AppComponent } from './app.component';
import { AuthGuard } from './services/auth/authguard';
import { AuthService, AuthProvider } from './services/auth/auth.service';
import { AuthHttp, AuthConfig, AUTH_PROVIDERS, provideAuth } from 'angular2-jwt';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { NavigationModule } from './components/navigation/navigation.module'
import { ExceedanceModule } from './components/exceedance/exceedance.module';
import { DirectivesModule } from './components/directives/directives.module';
import { UserModule } from './components/user/user.module';
import { FileUploadService } from './services/file/file-upload.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'

@NgModule({
	declarations: [
		AppComponent,
		DashboardComponent
	],
	imports: [
		// BrowserModule,
		// FormsModule,
		// HttpModule,
		// RouterModule,
		//MaterialModule.forRoot(),
		//AppRoutes,
		BrowserAnimationsModule, // This required to use angular/material@2.0.0-beta.2 with Angular 4.
		CommonModule,
		FlexLayoutCustomBreakpointsModule,
		Routing,
		PublicModule,
		NavigationModule,
		DirectivesModule,
		ExceedanceModule,
		InstrumentModule,
		MobileModule,
		ReportModule,
		SettingsModule,
		UserModule,
		UnverifiedDataModule
	],
	entryComponents: [
		OkDialogComponent,
		YesNoDialogComponent,
		UserDialogComponent,
		UserPasswordDialogComponent,
		UserStatusDialogComponent,
		UserGroupDialogComponent,
		ImeRecheckDialogComponent,
		ImeRepairDialogComponent,
		IseRecheckDialogComponent,
		IseRepairDialogComponent,
		InstrumentDialogComponent,
		InstrumentTypeDialogComponent,
		EditUnverifiedInstantaneousDialogComponent,
		EditUnverifiedWarmspotDialogComponent,
		EditUnverifiedIntegratedDialogComponent,
		EditUnverifiedProbeDialogComponent,
		ScheduledReportDialogComponent,
		SuperAdminPasswordDialogComponent
	],
	providers: [
		//AppRouterProviders,
		{provide: LocationStrategy, useClass: HashLocationStrategy},
		AuthService,
		AuthHttp,
		AuthProvider,
		AuthGuard,
		// AuthService,
		ApplicationSettingsCanDeactivate,
		ApplicationSettingsService,
		FileUploadService,
		FileDownloadService,
		ImeNumberService,
		InstantaneousDataService,
		IntegratedDataService,
		InstrumentService,
		InstrumentTypeService,
		IseNumberService,
		NavigationService,
		ScheduledEmailService,
		ReportService,
		TitleService,
		UnverifiedDataService,
		UserActivityService,
		UserGroupService,
		UserService,
		WarmspotDataService,
	],
	bootstrap: [AppComponent]
})

export class AppModule {

}
