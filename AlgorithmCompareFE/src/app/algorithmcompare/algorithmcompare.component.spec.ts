import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlgorithmcompareComponent } from './algorithmcompare.component';

describe('AlgorithmcompareComponent', () => {
  let component: AlgorithmcompareComponent;
  let fixture: ComponentFixture<AlgorithmcompareComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AlgorithmcompareComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AlgorithmcompareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
