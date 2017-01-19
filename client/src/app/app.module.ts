import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '@angular/material'
import { AppRoutes, Routing, AppRouterProviders } from './app.routing';
import { AppComponent } from './app.component';
import { AuthGuard } from './services/auth/authguard';
import { AuthService, AuthProvider } from './services/auth/auth.service';
import { AuthHttp, AuthConfig, AUTH_PROVIDERS, provideAuth } from 'angular2-jwt';

import { DashboardComponent } from './components/dashboard/dashboard.component';

import { PublicModule } from './components/public/public.module'
import { NavigationModule } from './components/navigation/navigation.module'
import { DirectivesModule } from './components/directives/directives.module';
import { UserModule } from './components/user/user.module';
import { TestModule } from './components/test/test.module';

import { UserService } from './services/user-service';
import { InstantaneousDataService } from './services/instantaneous-data.service';
import { SitesService } from './services/sites.service';
import { FileUploadService } from './services/file-upload.service';

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
		Routing,
		PublicModule,
		NavigationModule,
		DirectivesModule,
		UserModule,
		TestModule,
	],
	providers: [
		//AppRouterProviders,
		AuthHttp,
		AuthProvider,
		AuthGuard,
		AuthService,
		UserService,
		FileUploadService,
		InstantaneousDataService,
		SitesService,
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
