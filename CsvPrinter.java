package it.unibz.testhunter;

import it.unibz.testhunter.model.TestResultModel;
import it.unibz.testhunter.shared.TException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class CsvPrinter {

	public static void printPairList(List<Pair<Float>> list,
			String headerX, String headerY, OutputStreamWriter writer) throws TException {
		try {
			writer.append(headerX);
			writer.append(',');
			writer.append(headerY);
			writer.append('\n');

			for (@SuppressWarnings("rawtypes")
			Pair p : list) {
				writer.append(p.getX().toString());
				writer.append(',');
				writer.append(p.getY().toString());
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("unable to print");
		}
	}

	public static void printSequence(List<TestResultModel> seq,
			OutputStreamWriter writer) throws TException {
		try {
			writer.append("position");
			writer.append(',');

			writer.append("info");
			writer.append(',');

			writer.append("time");
			writer.append(',');

			writer.append("tangent");
			writer.append('\n');

			Long i = new Long(0);
			for (TestResultModel x : seq) {
				writer.append(i.toString());
				i++;
				writer.append(',');

				writer.append(x.getInfo().toString());
				writer.append(',');

				writer.append(x.getTime().toString());
				writer.append(',');

				writer.append(x.getTangent().toString());

				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw new TException(e.getMessage()).setUserMsg("unable to print");
		}
	}

	public static void printGraphic(List<TestResultModel> seq,
			OutputStreamWriter writer) throws TException {
		try {

			Float info = new Float(0);
			Float time = new Float(0);
			Float area = new Float(0);
			Float half = new Float(0.5);
			Float zero = new Float(0);
			Float T = new Float(0);
			Float I = new Float(0);
			Long i = new Long(0);

			for (TestResultModel x : seq) {
				T += x.getTime();
				I += x.getInfo();
			}
			writer.append("index");
			writer.append(',');

			writer.append("percent of time");
			writer.append(',');

			writer.append("percent of entropy reduction");
			writer.append(',');

			writer.append("tangent");
			writer.append(',');

			writer.append("aper");
			writer.append('\n');

			for (TestResultModel x : seq) {

				writer.append(i.toString());
				writer.append(',');

				writer.append(String.valueOf(time / T));
				writer.append(',');

				writer.append(String.valueOf(info / I));
				writer.append(',');

				writer.append(x.getTangent().toString());
				writer.append(',');

				area += (half * x.getInfo() + info) * x.getTime();
				Float aper = ((time * info) > 0) ? area / (time * info) : zero;

				writer.append(aper.toString());
				writer.append('\n');

				info += x.getInfo();
				time += x.getTime();
				i++;
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new TException(e.getMessage()).setUserMsg("unable to print");
		}
	}

}
