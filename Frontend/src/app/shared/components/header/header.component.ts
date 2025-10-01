import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserService } from 'src/app/Services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  @Input() 
  navItems: any;
  subscription: Subscription | undefined;
  constructor(private _router: Router , private userService:UserService) { }

  ngOnInit(): void {
  }

  navigateTo(route: any) {
    this._router.navigate([route]);
  }

  returntologin(){
    this.userService.removeToken();
    localStorage.removeItem('userQuery');
    localStorage.removeItem('aiResponse');
    localStorage.removeItem('loadingState');
    localStorage.removeItem('sessionActive');
    this._router.navigateByUrl('login');
  }
}
