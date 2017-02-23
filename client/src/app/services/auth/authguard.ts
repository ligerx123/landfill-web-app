import { AuthService } from './auth.service';
import { tokenNotExpired, JwtHelper } from 'angular2-jwt';
import { CanActivate, Router, ActivatedRouteSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { RestrictedRouteBase } from './../../app.routing';
import { UserRole } from './../../model/server/model/user-role.enum';

@Injectable()
export class AuthGuard implements CanActivate {

	constructor(
		private router: Router,
		private authService:AuthService
	) {}

	canActivate(route:ActivatedRouteSnapshot):boolean {

		console.log(route.data);

		if (this.authService.isTokenExpired()) {
			console.log("AuthGuard: JWT Token is expired...")
			this.router.navigate(['/login']);
			// TODO Add inactivity message.
			return false;
		}

		if (this.authService.canAccess(route.data["roles"])) {
			console.log("AuthGuard: Allowing access because user is an admin or has a required role.");
			return true;
		}

		console.log("AuthGuard: Denying access because user doesn't have any of the requred roles.")
		this.router.navigate([RestrictedRouteBase + '/forbidden']);
		return false;

	}

}