import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Biber } from './biber';

describe('Biber', () => {
  let component: Biber;
  let fixture: ComponentFixture<Biber>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Biber]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Biber);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
