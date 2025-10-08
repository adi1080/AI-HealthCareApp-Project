import { Component } from '@angular/core';

declare var bootstrap: any; 

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  adminNav: any[] = [];

  constructor() { }


  ngOnInit(): void {
    this.adminNav = [
      {
        name: 'Home',
        route: 'admin/Home',
        data: null,
      },
      {
        name: 'Ask AI',
        route: '/AI',
        data: null,
      },
    ];
  
  }

}
