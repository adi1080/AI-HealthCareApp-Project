import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocInfoComponent } from './doc-info.component';

describe('DocInfoComponent', () => {
  let component: DocInfoComponent;
  let fixture: ComponentFixture<DocInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DocInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
