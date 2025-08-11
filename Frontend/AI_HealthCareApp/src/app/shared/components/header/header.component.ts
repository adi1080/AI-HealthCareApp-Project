import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  @Input() 
  navItems: any;
  subscription: Subscription | undefined;
  constructor(private _router: Router) { }

  ngOnInit(): void {
  }

  navigateTo(route: any) {
    this._router.navigate([route]);
  }

  returntologin(){
    localStorage.removeItem('userQuery');
    localStorage.removeItem('aiResponse');
    localStorage.removeItem('loadingState');
    localStorage.removeItem('sessionActive');
    this._router.navigateByUrl('login');
  }
}
