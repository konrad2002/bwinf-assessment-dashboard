import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BiberStage } from './biber-stage';

describe('BiberStage', () => {
  let component: BiberStage;
  let fixture: ComponentFixture<BiberStage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BiberStage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BiberStage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
