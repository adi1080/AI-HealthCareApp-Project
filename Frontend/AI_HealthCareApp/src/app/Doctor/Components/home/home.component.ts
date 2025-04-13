import { Component } from '@angular/core';

declare var bootstrap: any; 

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
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

    // scrollToHeroCarousel(){
  //   const element = document.getElementById('hero-carousel');

  //   if(element){
  //     element.scrollIntoView({behavior: 'smooth' , block : 'start'})
  //   }
  // }
}
