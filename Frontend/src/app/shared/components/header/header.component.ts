import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { UserService } from 'src/app/Services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  @Input() navItems: any[] = [];
  currentRoute: string = '';
  subscription: Subscription | undefined;

  constructor(private _router: Router, private userService: UserService) {}

  ngOnInit(): void {
    // Set current route initially
    this.currentRoute = this._router.url;

    // Listen to route changes and update currentRoute
    this.subscription = this._router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd)
      )
      .subscribe(event => {
        this.currentRoute = event.urlAfterRedirects;
      });
  }

  navigateTo(route: string) {
    this._router.navigate([route]);
  }

  returntologin() {
    this.userService.removeToken();
    localStorage.removeItem('userQuery');
    localStorage.removeItem('aiResponse');
    localStorage.removeItem('loadingState');
    localStorage.removeItem('sessionActive');
    this._router.navigateByUrl('login');
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}
