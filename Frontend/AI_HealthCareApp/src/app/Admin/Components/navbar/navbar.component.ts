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
    // // Reinitialize the carousel when the component is initialized
    // const myCarouselElement = document.getElementById('hero-carousel');
    // if (myCarouselElement) {
    //   const carousel = new bootstrap.Carousel(myCarouselElement, {
    //     interval: 2000,  // Optional: Controls the interval between auto sliding
    //     ride: 'carousel' // Optional: Starts the carousel automatically when page loads
    //   });
    // }

    this.adminNav = [
      {
        name: 'Home',
        route: '/patient/Home',
        data: null,
      },
      {
        name: 'Ask AI',
        route: '/patient/AI',
        data: null,
      },
    ];
  
  }

}
