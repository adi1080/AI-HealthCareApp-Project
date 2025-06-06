import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocFeedbackComponent } from './doc-feedback.component';

describe('DocFeedbackComponent', () => {
  let component: DocFeedbackComponent;
  let fixture: ComponentFixture<DocFeedbackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocFeedbackComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DocFeedbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
