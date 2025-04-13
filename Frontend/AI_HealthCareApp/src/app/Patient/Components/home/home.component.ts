import { Component } from '@angular/core';
import { Router } from '@angular/router';

declare var bootstrap: any; 

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  constructor(private router:Router){ }
    
  ngOnInit(): void {
    // Reinitialize the carousel when the component is initialized
    const myCarouselElement = document.getElementById('hero-carousel');
    if (myCarouselElement) {
      const carousel = new bootstrap.Carousel(myCarouselElement, {
        interval: 2000,  // Optional: Controls the interval between auto sliding
        ride: 'carousel' // Optional: Starts the carousel automatically when page loads
      });
    }
  }

  logout(){
    this.router.navigateByUrl('login');
  }

  scrollToHeroCarousel(){
    const element = document.getElementById('hero-carousel');

    if(element){
      element.scrollIntoView({behavior: 'smooth' , block : 'start'})
    }
  }
}
