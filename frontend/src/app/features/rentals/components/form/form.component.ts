import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { RentalResponse } from '../../interfaces/api/rentalResponse.interface';
import { Rental } from '../../interfaces/rental.interface';
import { RentalsService } from '../../services/rentals.service';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {

  public onUpdate: boolean = false;
  public rentalForm: FormGroup | undefined;
  public pictureError: string | null = null;
  public descriptionError: string | null = null;

  private id: string | undefined;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private matSnackBar: MatSnackBar,
    private rentalsService: RentalsService,
    private sessionService: SessionService,
    private router: Router
  ) {
  }

  public ngOnInit(): void {
    const url = this.router.url;
    if (url.includes('update')) {
      this.onUpdate = true;
      this.id = this.route.snapshot.paramMap.get('id')!;
      this.rentalsService
        .detail(this.id)
        .subscribe((rental: Rental) => this.initForm(rental));
    } else {
      this.initForm();
    }
  }

  public submit(): void {
    if (this.pictureError) {
      return;
    }
    const formData = new FormData();
    formData.append('name', this.rentalForm!.get('name')?.value);
    formData.append('surface', this.rentalForm!.get('surface')?.value);
    formData.append('price', this.rentalForm!.get('price')?.value);
    if (!this.onUpdate) {
      formData.append('picture', this.rentalForm!.get('picture')?.value._files[0]);
    }
    formData.append('description', this.rentalForm!.get('description')?.value);

    if (!this.onUpdate) {
      this.rentalsService
        .create(formData)
        .subscribe((rentalResponse: RentalResponse) => this.exitPage(rentalResponse));
    } else {
      this.rentalsService
        .update(this.id!, formData)
        .subscribe((rentalResponse: RentalResponse) => this.exitPage(rentalResponse));
    }
  }

  onPictureSelected(event: any) {
    const file = event.target.files?.[0];
    if (!file) {
      this.pictureError = null;
      this.rentalForm!.get('picture')?.setValue('');
      return;
    }
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
    if (!allowedTypes.includes(file.type)) {
      this.pictureError = "Seuls les fichiers jpg, png ou gif sont autorisés.";
      this.rentalForm!.get('picture')?.setValue('');
    } else {
      this.pictureError = null;
      // ngx-mat-file-input gère déjà la valeur
    }
  }

  private initForm(rental?: Rental): void {
    console.log(rental);
    console.log(this.sessionService.user!.id);
    if( (rental !== undefined) && (rental?.owner_id !== this.sessionService.user!.id)) {
      this.router.navigate(['/rentals']);
    }
    this.rentalForm = this.fb.group({
      name: [rental ? rental.name : '', [Validators.required]],
      surface: [rental ? rental.surface : '', [Validators.required]],
      price: [rental ? rental.price : '', [Validators.required]],
      description: [rental ? rental.description : '',  [Validators.required, Validators.maxLength(2000)]]
    });
    if (!this.onUpdate) {
      this.rentalForm.addControl('picture', this.fb.control('', [Validators.required]));
    }
    this.rentalForm.get('description')?.valueChanges.subscribe(value => {
      if (value && value.length > 2000) {
        this.descriptionError = 'La description ne doit pas dépasser 2000 caractères.';
      } else {
        this.descriptionError = null;
      }
    });
  }

  private exitPage(rentalResponse: RentalResponse): void {
    this.matSnackBar.open(rentalResponse.message, "Close", { duration: 3000 });
    this.router.navigate(['rentals']);
  }
}