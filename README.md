# discounts
Calculate the cost applying the best available discount

Includes 3 Rest Api Calls.

* <b>Add a discount</b> <br/>
	Http Post: http://localhost:8080/poc/api/discounts/v1/ <br/>
	Sample Input: <br/>
	[
    		{
        		"code": "ABC",
        		"category": "by_type",
        		"type": "clothes",
        		"condition": 0,
        		"percentage": 10
    		}
	] <br/>
	Sample Output:<br/>
	{
    		"status": "Success",
    		"httpStatus": "OK",
    		"message": null,
    		"httpStatusCode": 200
	}<br/>
  
* <b>Delete a discount</b> <br/>
  Http Delete: http://localhost:8080/poc/api/discounts/v1/ABC <br/>
  	Sample Output:<br/>
	{
    		"status": "Success",
    		"httpStatus": "OK",
    		"message": null,
    		"httpStatusCode": 200
	}<br/>
  
* <b>Get the cost with applicable discount </b><br/>
 Http Get: http://localhost:8080/poc/api/discounts/v1/get-best-applicable-discount <br/>
 Header <items: [{"id": 123,"cost": 50,"type": "clothes","count": 5}]> <br/>
 Sample Output : <br/>
 {
    "status": "Success",
    "httpStatus": "OK",
    "message": "{"totalCost":200.0,"isDiscountApplied":true,"discountApplied":"{"code":"FGH","category":"by_count","type":"clothes","condition":2,"percentage":20,"createdBy":"system","createdDate":"Aug 31, 2022, 9:19:39 AM"}"}",
    "httpStatusCode": 200
}<br/>
 

